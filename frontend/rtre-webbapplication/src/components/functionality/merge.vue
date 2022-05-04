<template>
    <v-card flat>
              <v-card-text>
                <h1 class="my-5">first merge file:</h1>
                <v-divider></v-divider>
                <FileDrop @update:file="updateFileNameVariable2" />
                <h1 class="my-5">Second merge file:</h1>
                <v-divider></v-divider>
                <FileDrop @update:file="updateFileNameVariable3" />
                <h1 class="my-5">Output file:</h1>
                <v-divider></v-divider>
                <FileDrop @update:file="updateFileNameVariable4" />
                 <v-btn text class="blue mx-0 mt-3" @click="merge"
                    >Merge ProjectsS</v-btn
                  >
              </v-card-text>
            </v-card>
</template>
<script>
import FileDrop from "../FileDrop.vue";
export default {
      components: { FileDrop },
      name: "FunctionHub",
      data(){
          return{
            file1: null,
            file2: null,
            outputFile: null,
          }
      },
      methods:{
        updateFileNameVariable2(value) {
        this.file1 = value;
        },
        updateFileNameVariable3(value) {
        this.file2 = value;
        },
        updateFileNameVariable4(value) {
        this.outputFile = value;
        },
        merge(){
        fetch(
            "http://localhost:3030/api/merge?mergeFile1="+this.file1+"&mergeFile2="+this.file2+"&outputFile="+this.outputFile
        )
        },
      }
}
</script>

