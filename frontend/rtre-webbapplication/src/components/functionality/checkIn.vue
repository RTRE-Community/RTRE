<template>
     <v-card flat>
              <v-card-text >
                <FileDrop @update:file="updateFileNameVariable1" />
              </v-card-text>
              <v-select
          :items="items"
          v-model="selectedFormat"
          @click="testPrint"
          dense 
          label="Ifc format"
          outlined
        ></v-select>
              <v-btn
                depressed
                color="blue"
                @select="checkIn(fileForCheckin)"
              >
                Submit
              </v-btn>
            </v-card>
</template>

<script>
import FileDrop from "../FileDrop.vue";
export default {
      components: { FileDrop },
    name:"CheckIn",
      data() {
    return {
      fileForCheckin: null,
      selectedFormat: "",
      items:["Ifc4", "Ifc2x3tc1"]
    };
  },
  methods: {
    updateFileNameVariable1(value) {
      this.fileForCheckin = value;
      console.log(this.fileForCheckin);
    },
    testPrint(){
        console.log(this.selectedFormat)
    },
    checkIn() {
      fetch(
        "http://localhost:3030/api/postIfc?fileName=" +
          this.fileForCheckin
      );
      window.location.reload();
    },
  },
}
</script>