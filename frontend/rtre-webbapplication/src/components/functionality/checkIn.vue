<template>
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
</template>

<script>
import FileDrop from "../FileDrop.vue";
export default {
      components: { FileDrop },
    name:"CheckIn",
      data() {
    return {
      fileForCheckin: null,
    };
  },
  methods: {
    updateFileNameVariable1(value) {
      this.fileForCheckin = value;
      console.log(this.fileForCheckin);
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