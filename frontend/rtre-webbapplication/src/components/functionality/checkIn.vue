<template>
     <v-card class="pb-4">
              <v-card-text >
                <FileDrop @update:file="updateFileNameVariable1" />
              </v-card-text>
              <v-select
          :items="items"
          v-model="selectedFormat"
          dense 
          label="Ifc format - WIP"
          outlined
          class="mx-10"
        ></v-select>
              <v-btn
                color="blue"
                @click="checkIn()"
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
            console.log(this.selectedFormat)
    },
    checkIn() {
      fetch(
        "http://localhost:3030/api/postIfc?fileName=" +
          this.fileForCheckin+"&schema="+this.selectedFormat
      );
      console.log( "http://localhost:3030/api/postIfc?fileName=" +
          this.fileForCheckin+"&schema="+this.selectedFormat)
    },
  },
}
</script>