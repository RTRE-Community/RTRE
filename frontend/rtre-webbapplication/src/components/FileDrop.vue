<template>
  <v-card>
    <v-file-input
      truncate-length="15"
      label="Choose Ifc file"
      v-model="file"
      @change="fileInformation"
    ></v-file-input>
  </v-card>
</template>

<script>
export default {
  name: "FileDrop",
  data() {
    return {
      file: null,
    };
  },
  methods: {
    fileInformation() {
      this.$emit("update:file", this.file.name);
    },
  },
};
</script>

<style></style>
