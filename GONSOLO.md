# These have to be exported:

export PDK_ROOT=~/work/KianV/tt/pdk
export PDK=ihp-sg13g2
export OPENLANE_IMAGE_OVERRIDE=ghcr.io/tinytapeout/openlane2:ihp-v3.0.0.dev23

# Install from Arch:

abc
wasmtime
klayout

# Install from Arch AUR:

openlane2
python-libparse
python-volare
python-yamlcore
yowasp-runtime
yowasp-yosys

python-wasmtime
python-cloup
cudd
openroad-git
magic

# TODO

/usr/lib/python3.13/site-packages/openlane/steps/klayout.py changes

klayout -b -rd output=/home/gonsolo/work/KianV/runs/wokwi/58-klayout-streamout/tt_um_kianV_rv32ima_uLinux_SoC.klayout.gds   -rd top=tt_um_kianV_rv32ima_uLinux_SoC  -rd lyp=/home/gonsolo/work/KianV/tt/pdk/ihp-sg13g2/libs.tech/klayout/tech/sg13g2.lyp -rd lyt=/home/gonsolo/work/KianV/tt/pdk/ihp-sg13g2/libs.tech/klayout/tech/sg13g2.lyt -rd lym=/home/gonsolo/work/KianV/tt/pdk/ihp-sg13g2/libs.tech/klayout/tech/sg13g2.map -rd input-lef=/home/gonsolo/work/KianV/tt/pdk/ihp-sg13g2/libs.ref/sg13g2_stdcell/lef/sg13g2_tech.lef -rd input-lef=/home/gonsolo/work/KianV/tt/pdk/ihp-sg13g2/libs.ref/sg13g2_stdcell/lef/sg13g2_stdcell.lef -rd with-gds-file=/home/gonsolo/work/KianV/tt/pdk/ihp-sg13g2/libs.ref/sg13g2_stdcell/gds/sg13g2_stdcell.gds -r /usr/lib/python3.13/site-packages/openlane/scripts/klayout/stream_out.py /home/gonsolo/work/KianV/runs/wokwi/53-odb-cellfrequencytables/tt_um_kianV_rv32ima_uLinux_SoC.def
