<template>
<v-card class="pb-4">
    <v-card-text>
        <v-file-input label="upload local file: " truncate-length="15" v-model="fileupload"></v-file-input>
    </v-card-text>
    <v-text-field v-model="parentOid" label="Head Project Id" outlined class="shrink mx-11"></v-text-field>
    <v-select :items="items" v-model="selectedFormat" dense label="Ifc schema" outlined class="mx-11"></v-select>
    <v-btn class="ml-11" color="blue white--text" @click="checkIn()" :loading="loading">
        Submit
    </v-btn>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import SnackBar from './buttons/SnackBar.vue';
export default {
    name: "CheckIn",
    data() {
        return {
            fileForCheckin: null,
            parentOid: null,
            selectedFormat: "",
            items: ["Ifc4", "Ifc2x3tc1"],
            fileupload: [],
            response:"",
            loading:false
        };
    },
    methods: {
        async checkIn() {
            this.loading = true
            let formData = new FormData();
            formData.append("file", this.fileupload);
            this.response = await fetch("http://localhost:3030/api/postIfcAsSubProject?fileName=" +
                this.fileForCheckin + "&schema=" + this.selectedFormat + "&parentPoid=" + this.parentOid, {
                method: "POST",
                body: formData
            }).then(this.loading = false);
        },
    },
    components: { SnackBar }
}
</script>
