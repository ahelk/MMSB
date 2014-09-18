import sys
from random import randrange
if __name__ == "__main__":
    num = int(sys.argv[1])
    f = open('input.txt', 'w')
    f.write(str(num)+"\n")
    for i in xrange(num):
        x = randrange(num)
        connecting = set()
        for j in xrange(x):
            connecting.add(randrange(num))
        connecting = list(connecting)
        f.write(" ".join(map(str, connecting))+"\n")

