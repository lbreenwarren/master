#test cmpxchg
export mainx20
export answer0
export answer1
  jmp mainx20
answer0:
  word 0
answer1:
  word 0
lock:
  word 0

mainx20:
  getpid   r8
  ldimm    r3, 5
  ldimm	   r5, 10
  ldimm    r4, 0           # loop counter
  ldimm    r6, 1           # loop counter increment
  ldimm    r9, 0		   # match pid=0
  ldimm    r7, 500           # upper bound
doitagain:
#   busy wait on the lock
  ldimm   r1, 1            # want to set the lock to 1
tryAgain:
  ldimm   r2, 0            # need to wait until lock is 0
  cmpxchg r2, r1, lock     # if lock is 0 then lock it
  beq     r2, r1, tryAgain # else r2 will be set to 1, and if so repeat
  beq	  r8, r9, use0
  jmp	  use1	

continue:  ldimm   r2, 0   # unlock
  store   r2, lock

  addi    r4, r6
  blt     r4, r7, doitagain
  jmp     alldone

use0:
  load    r0, answer0
  addi    r0, r3
  store   r0, answer0
  jmp     continue
use1:
  load    r0, answer1
  addi    r0, r5
  store   r0, answer1
  jmp     continue

# all done!
alldone: halt



