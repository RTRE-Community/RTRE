<template>
  <v-card class="pb-4">
    <v-form ref="form">
      <v-card-text>
        <v-file-input
          :rules="ruleFiles"
          label="upload local file: "
          truncate-length="15"
          v-model="fileupload"
          name="fileButtonCheckIn"
        ></v-file-input>
      </v-card-text>
      <v-text-field
        :rules="ruleInput"
        v-model="parentOid"
        label="Head Project Id"
        outlined
        class="shrink mx-11"
      ></v-text-field>
      <v-select
        :rules="ruleSelector"
        :items="items"
        v-model="selectedFormat"
        dense
        label="Ifc schema"
        outlined
        name="CheckInSelector"
        class="mx-11"
      ></v-select>
      <v-btn
        class="ml-11"
        color="blue white--text"
        @click="checkIn()"
        :loading="loading[0]"
      >
        Submit
      </v-btn>
    </v-form>
    <SnackBar :response="response"></SnackBar>
  </v-card>
</template>

<script>
import SnackBar from "./buttons/SnackBar.vue";
import Vue from "vue";
export default {
  name: "CheckIn",
  data() {
    return {
      ruleFiles: [(value) => value.size >= 0 || "need a file!"],
      ruleInput: [(value) => !!value || "input an id"],
      ruleSelector: [(value) => !!value || "Select a schema"],
      fileForCheckin: null,
      parentOid: null,
      selectedFormat: "",
      items: ["Ifc4", "Ifc2x3tc1"],
      fileupload: [],
      response: "",
      loading: [false],
    };
  },
  methods: {
    async checkIn() {
      if (this.$refs.form.validate()) {
        Vue.set(this.loading, 0, true);
        let formData = new FormData();
        formData.append("file", this.fileupload);
        this.response = await fetch(
          process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/postIfcAsSubProject?schema=" +
            this.selectedFormat +
            "&parentPoid=" +
            this.parentOid,
          {
            method: "POST",
            body: formData,
          }
        );
      }
      Vue.set(this.loading, 0, false);
    },
  },
  components: {
    SnackBar,
  },
};
</script>
