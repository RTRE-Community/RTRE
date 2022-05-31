<template>
<v-card flat>
    <v-card-text>
        <v-form>
            <v-text-field label="id" v-model="id" outlined class="shrink mx-11"></v-text-field>
            <v-btn text class="blue white--text mx-0 mt-3" @click="checkOut">Get Project</v-btn>
        </v-form>
    </v-card-text>
</v-card>
</template>

<script>
import FileDownload from "js-file-download"
import Axios from "axios"
export default {
    name: "checkOut",
    data() {
        return {
            id: "",
        }
    },
    methods: {
        checkOut() {
            Axios({
            url:  "http://localhost:3030/api/getIfc?fileName=" + this.id,
            methods:"GET",
            responseType:"blob"
                })
            .then((res) => {FileDownload(res.data,"myIfcFile.ifc")});
        },
    }
}
</script>
