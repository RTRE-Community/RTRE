
import ifcpatch

print("Directory of first merge file; ")
mergeFile1 = input()
print("Directory of Second merge file; ")
mergeFile2 = input()
print("Directory of empty file for output; ")
outputFile = input()


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