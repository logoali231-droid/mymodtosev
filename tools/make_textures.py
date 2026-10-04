#!/usr/bin/env python3
"""
make_textures.py - Gera texturas do SevTech Helpers a partir das vanilla.
Uso:
    python tools/make_textures.py
    python tools/make_textures.py --jar /caminho/para/1.12.2.jar
"""
import argparse
import os
import sys
import zipfile
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    print("ERRO: instale Pillow -> pip install Pillow")
    sys.exit(1)

# ---------------------------------------------------------------
# CONFIG
# ---------------------------------------------------------------
MOD_ID = "sevtechhelpers"
RES_ROOT = Path("src/main/resources/assets") / MOD_ID
TEX_BLOCK = RES_ROOT / "textures/blocks"
TEX_ITEM  = RES_ROOT / "textures/items"
TEX_ENTITY = RES_ROOT / "textures/entity"

# Mapeamento: arquivo vanilla -> (destino, hue_alvo, sat_boost, val_boost)
# hue_alvo em graus (0-360). None = não altera.
JOBS = [
    # arquivo vanilla                  nome destino         hue   sat  val
    ("assets/minecraft/textures/blocks/planks_oak.png",  "blocks/totem.png",           280, 0.7, 0.75),  # roxo escuro
    ("assets/minecraft/textures/items/stick.png",        "items/helper_whistle.png",   45,  0.9, 1.15),  # dourado
    ("assets/minecraft/textures/entity/slime/slime.png", "entity/lumberjack.png",      30,  0.8, 0.85),  # marrom
    ("assets/minecraft/textures/entity/slime/slime.png", "entity/miner.png",           210, 0.7, 0.80),  # azul
    ("assets/minecraft/textures/entity/slime/slime.png", "entity/farmer.png",          90,  0.9, 0.95),  # verde
    ("assets/minecraft/textures/entity/slime/slime.png", "entity/hunter.png",          0,   0.9, 0.90),  # vermelho
]

# ---------------------------------------------------------------
# ENCONTRA O JAR DO MINECRAFT
# ---------------------------------------------------------------
def find_minecraft_jar(explicit=None):
    if explicit:
        p = Path(explicit)
        if p.exists(): return p
        print(f"Jar informado não existe: {p}"); sys.exit(1)

    candidates = []

    # 1) Cache do Gradle do projeto
    for base in [Path.home()/".gradle", Path.cwd()/".gradle"]:
        if base.exists():
            for f in base.rglob("minecraft-1.12.2*.jar"):
                if "sources" not in f.name and "javadoc" not in f.name:
                    candidates.append(f)

    # 2) Instalação padrão do Minecraft
    for base in [
        Path.home()/".minecraft/versions/1.12.2",
        Path(os.environ.get("APPDATA", ""))/".minecraft/versions/1.12.2",
        Path.home()/"Library/Application Support/minecraft/versions/1.12.2",
    ]:
        if base.exists():
            for f in base.glob("*.jar"):
                if not f.name.endswith("-natives.jar"):
                    candidates.append(f)

    if not candidates:
        print("Não achei o jar do MC 1.12.2 automaticamente.")
        print("Rode com: python tools/make_textures.py --jar <caminho>")
        sys.exit(1)

    # Prefere o maior (é o jar real, não o obfuscado pequeno)
    candidates.sort(key=lambda p: p.stat().st_size, reverse=True)
    return candidates[0]

# ---------------------------------------------------------------
# RECOLORAÇÃO
# ---------------------------------------------------------------
def recolor(img, target_hue, sat_boost=1.0, val_boost=1.0):
    """Recolore a imagem inteira mantendo luminosidade relativa.
    Preserva pixels pretos/brancos/quase-acromáticos (olhos, contornos)."""
    img = img.convert("RGBA")
    px = img.load()
    w, h = img.size
    for y in range(h):
        for x in range(w):
            r, g, b, a = px[x, y]
            if a == 0:
                continue
            # Converte RGB -> HSV
            import colorsys
            hh, ss, vv = colorsys.rgb_to_hsv(r/255.0, g/255.0, b/255.0)

            # Pixel quase acromático (olho/contorno) -> preserva
            if ss < 0.15:
                continue

            # Aplica transformação
            hh = (target_hue % 360) / 360.0
            ss = min(1.0, ss * sat_boost)
            vv = min(1.0, vv * val_boost)

            r2, g2, b2 = colorsys.hsv_to_rgb(hh, ss, vv)
            px[x, y] = (int(r2*255), int(g2*255), int(b2*255), a)
    return img

# ---------------------------------------------------------------
# MAIN
# ---------------------------------------------------------------
def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--jar", help="Caminho do minecraft-1.12.2.jar")
    args = ap.parse_args()

    jar = find_minecraft_jar(args.jar)
    print(f"[+] Usando jar: {jar}")

    # Cria pastas
    TEX_BLOCK.mkdir(parents=True, exist_ok=True)
    TEX_ITEM.mkdir(parents=True, exist_ok=True)
    TEX_ENTITY.mkdir(parents=True, exist_ok=True)

    from io import BytesIO

    with zipfile.ZipFile(jar) as zf:
        for src, dst_rel, hue, sat, val in JOBS:
            try:
                data = zf.read(src)
            except KeyError:
                print(f"[!] Não achei {src} no jar, pulando.")
                continue
            img = Image.open(BytesIO(data))
            out = recolor(img, hue, sat, val)
            dst = RES_ROOT / "textures" / dst_rel
            dst.parent.mkdir(parents=True, exist_ok=True)
            out.save(dst)
            print(f"[+] {src}  ->  {dst}")

    print("\n[OK] Texturas geradas. Compile com ./gradlew build")

if __name__ == "__main__":
    main()