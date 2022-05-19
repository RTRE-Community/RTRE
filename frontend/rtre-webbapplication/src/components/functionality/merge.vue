<template>
<v-card flat>
    <v-card-text>
        <h1 class="my-2">First merge file:</h1>
        <v-text-field label="Solo" solo dense v-model="file1"></v-text-field>
        <h1 class="my-2">Second merge file:</h1>
        <v-text-field label="Solo" solo dense v-model="file2"></v-text-field>
        <v-select :items="items" v-model="selectedFormat" label="Ifc schema" dense outlined></v-select>
        <v-btn text class="blue white--text mx-0 mt-3" @click="merge">Merge Projects</v-btn>
    </v-card-text>
</v-card>
</template>

<script>
export default {
    components: {},
    name: "FunctionHub",
    data() {
        return {
            file1: null,
            file2: null,
            items: ["Ifc4", "Ifc2x3tc1"],
            selectedFormat: ""

        }
    },
    methods: {
        updateFileNameVariable2(value) {
            this.file1 = value;
        },
        updateFileNameVariable3(value) {
            this.file2 = value;
        },
        updateFileNameVariable4(value) {
            this.outputFile = value;
        },
        merge() {
            fetch(
                "http://localhost:3030/api/merge?mergeFile1=" + this.file1 + "&mergeFile2=" + this.file2 + "&ifcSchema=" + this.selectedFormat
            )
        },
    }
}
</script>
