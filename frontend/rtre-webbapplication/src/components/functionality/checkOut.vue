<template>
<v-card flat>
    <v-card-text>
        <v-form>
            <v-text-field label="id" v-model="id" outlined  class="shrink mx-11"></v-text-field>
            <v-text-field v-model="projectName" label="Project Name" outlined class="shrink mx-11"></v-text-field>
            <v-select :items="items" v-model="selectedFormat" label="Ifc schema" dense outlined></v-select>
            <v-btn text class="blue white--text mx-0 mt-3" @click="checkOut">Get Project</v-btn>
        </v-form>
    </v-card-text>
</v-card>
</template>

<script>
export default {
    name: "checkOut",
    data() {
        return {
            id: "",
            projectName: "",
            selectedFormat: "",
            items: ["Ifc4", "Ifc2x3tc1"]
        }
    },
    methods: {
        checkOut() {
            console.log(this.projectName);
            fetch("http://localhost:3030/api/getIfc?fileName=" + this.id +
                "&schema=" + this.selectedFormat +"&localName="+this.projectName);
        },
    }
}
</script>
