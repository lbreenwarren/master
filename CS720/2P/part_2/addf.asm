#program to add two floating point numbers
export result
export word1
export word2
mainx20:
  jmp start
result:
  word 0
word1:
  word 0
word2:
  word 0
start:
  load   r1, word1
  load   r2, word2
  addf   r1, r2
done:
  store  r1, result
  halt

