
import ifcpatch
import sys, getopt

mergeFile1 = (sys.argv[1])
mergeFile2 = (sys.argv[2])
outputFile = (sys.argv[3])

ifcpatch.execute({
    "input": mergeFile1,
    "output": outputFile,
    "recipe": "MergeProject",
    "log": "ifcpatch.log",
    "arguments": [mergeFile2],
    })

### "input": "C:/Users/Dennis/Desktop/Program/BimServer/bolt.ifc",

### "output": "C:/Users/Dennis/Desktop/Program/BimServer/imHere.ifc",

###  "arguments": ["C:/Users/Dennis/Desktop/Program/BimServer/stud.ifc"],