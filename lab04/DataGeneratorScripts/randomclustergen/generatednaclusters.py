#!/usr/bin/python2

import sys, random, math

if len(sys.argv) < 2 or sys.argv[1] == 'help' or sys.argv[1] == '-h' or len(sys.argv) < 5:
  print "usage: ./script.py <clusters> <elements> <length> <output file>"
  sys.exit(0)

P_DIFF   = 0.2
clusters = int(sys.argv[1])
elements = int(sys.argv[2])
length   = int(sys.argv[3])
output   = sys.argv[4]

if elements < clusters:
  print "must have at least as many elements as clusters"
  sys.exit(0)

tempstring = []
allstrings = []

centers = []

for c in xrange(clusters):
  for l in xrange(length):
    nextChar = random.randint(1, 4)
    if nextChar == 1:
      tempstring.append('A')
    elif nextChar == 2:
      tempstring.append('T')
    elif nextChar == 3:
      tempstring.append('C')
    else:
      tempstring.append('G')

  nextCluster = ''.join(tempstring)
  centers.append(nextCluster)
  allstrings.append(nextCluster)
  tempstring = []

for c in xrange(elements - clusters):
  centerpoint = centers[random.randint(1, clusters) - 1]
  prob = math.floor(100 * abs(random.gauss(0, P_DIFF))) / 100.0
  while prob > 0.5:
    prob = math.floor(100 * abs(random.gauss(0, P_DIFF))) / 100.0
  
  for l in xrange(length):
    prtest   = random.random()
    nextChar = random.randint(1, 4)
    if prtest > prob:
      tempstring.append(centerpoint[l])
    elif nextChar == 1:
      tempstring.append('A')
    elif nextChar == 2:
      tempstring.append('T')
    elif nextChar == 3:
      tempstring.append('C')
    else:
      tempstring.append('G')

  nextCluster = ''.join(tempstring)
  allstrings.append(nextCluster)
  tempstring = []

f = open(output, 'wb')
for string in allstrings:
  f.write(string + '\n')
  print string

f.close()
