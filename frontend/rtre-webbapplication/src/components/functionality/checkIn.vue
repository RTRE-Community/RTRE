<template>
<v-card class="pb-4">
    <v-card-text>
        <FileDrop @update:file="updateFileNameVariable1" />
    </v-card-text>
    <v-text-field v-model="projectName" label="Project Name" outlined class="shrink mx-11"></v-text-field>
    <v-text-field v-model="parentOid" label="Head Project Id" outlined class="shrink mx-11"></v-text-field>
    <v-select :items="items" v-model="selectedFormat" dense label="Ifc schema" outlined class="mx-11"></v-select>
    <v-btn class="ml-11" color="blue white--text" @click="checkIn()">
        Submit
    </v-btn>
</v-card>
</template>

<script>
import FileDrop from "../FileDrop.vue";
export default {
    components: {
        FileDrop
    },
    name: "CheckIn",
    data() {
        return {
            fileForCheckin: null,
            parentOid: null,
            selectedFormat: "",
            projectName: "",
            items: ["Ifc4", "Ifc2x3tc1"]
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
                "http://localhost:3030/api/postIfcAsSubProject?fileName=" +
                this.fileForCheckin + "&schema=" + this.selectedFormat + "&parentPoid=" + this.parentOid + "&projectName=" + this.projectName
            ).then(window.location.reload());
        },
    },
}
</script>
