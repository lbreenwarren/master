#
# one instruction from each format

top:

jmp bottom

getpid r12

ldimm r11, 0x7BCDE

load r10, bottom

addf r9, r8

ldind r7, 0x7AFE(r6)

beq r5, r4, top

chunk: alloc 0x7F

onemore: word 0x7EDCBA98

ret
bottom: