<template>
  <v-card class="pa-12">
    <v-card>
      <v-card>
        <v-toolbar flat color="primary" dark>
          <v-toolbar-title>User Profile</v-toolbar-title>
        </v-toolbar>
        <v-tabs vertical>
          <v-tab>
            <v-icon left> mdi-archive-arrow-up </v-icon>
            Check in
          </v-tab>
          <v-tab>
            <v-icon left> mdi-cloud-download </v-icon>
            Check Out
          </v-tab>
          <v-tab>
            <v-icon left> mdi-call-merge </v-icon>
            Merge
          </v-tab>

          <v-tab-item>
            <v-card flat>
              <v-card-text>
                <FileDrop @update:file="updateFileNameVariable1" />
              </v-card-text>
              <v-btn
                depressed
                color="submit"
                class="ma-5"
                @click="checkIn(fileForCheckin)"
              >
                Submit
              </v-btn>
            </v-card>
          </v-tab-item>
          <v-tab-item>
            <v-card flat>
              <v-card-text>
                <label for="id">Project id:</label><br />
                <v-divider></v-divider>
                <v-form class="px-3">
                  <v-text-field label="id" v-model="id"></v-text-field>
                  <v-btn text class="blue mx-0 mt-3" @click="checkOut"
                    >Get Project</v-btn
                  >
                </v-form>
              </v-card-text>
            </v-card>
          </v-tab-item>
          <v-tab-item>
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
          </v-tab-item>
        </v-tabs>
      </v-card>
      <v-alert
        style="position: absolute; width: 240px; height: 50px"
        v-model="alert"
        close-text="Close Alert"
        color="indigo"
        transition="scroll-y-transition"
        dark
        dismissible
      >
        Success!
      </v-alert>
    </v-card>
  </v-card>
</template>

<script>
import FileDrop from "./FileDrop.vue";
export default {
  components: { FileDrop },
  name: "FunctionHub",
  data() {
    return {
      id: "",
      errorHandler: "",
      alert: false,
      fileForCheckin: null,
      file1: null,
      file2: null,
      outputFile: null,
    };
  },
  methods: {
    checkOut() {
      console.log(this.id);
      fetch("http://localhost:3030/api/getIfc?fileName=" + this.id);
      this.alert = !this.alert;
      setTimeout(() => {
        this.alert = !this.alert;
      }, "1000");
    },

    updateFileNameVariable1(value) {
      this.fileForCheckin = value;
      console.log(this.fileForCheckin);
    },
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

    checkIn() {
      fetch(
        "http://localhost:3030/api/postIfc?fileName=" +
          this.fileForCheckin
      );
      window.location.reload();
    },
  },
};
</script>

<style></style>
