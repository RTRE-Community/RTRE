<template>
<v-card class="pb-4">
    <v-card-text>
        <FileDrop @update:file="updateFileNameVariable1" />
    </v-card-text>
    <v-select :items="items" v-model="selectedFormat" dense label="Ifc schema" outlined class="mx-12"></v-select>
    <v-text-field v-model="parentOid" label="Head Project Id" outlined class="shrink mx-11"></v-text-field>
    <v-btn color="blue" @click="checkIn()">
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
                this.fileForCheckin + "&schema=" + this.selectedFormat + "&parentPoid=" + this.parentOid
            ).then(window.location.reload());
        },
    },
}
</script>
