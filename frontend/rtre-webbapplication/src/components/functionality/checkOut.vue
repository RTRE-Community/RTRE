<template>
<v-card flat>
    <v-card-text>
        <v-form ref="form">
            <v-text-field :rules="ruleInput" label="id" v-model="id" outlined class="shrink mx-11"></v-text-field>
            <v-btn text class="blue white--text mx-0 mt-3" @click="checkOut" :loading="loading">Get Project</v-btn>
        </v-form>
    </v-card-text>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import FileDownload from "js-file-download"
import Axios from "axios"
import SnackBar from "./buttons/SnackBar.vue";
export default {
    name: "checkOut",
    data() {
        return {
            ruleInput: [
                value => value >= 3 || 'input an id'
            ],
            id: "",
            response: "",
            loading: false
        };
    },
    methods: {
        async checkOut() {
            if (this.$refs.form.validate()) {
                this.loading = true
                let that = this;
                Axios({
                        url: "http://localhost:3030/api/getIfc?fileName=" + this.id,
                        methods: "GET",
                        responseType: "blob"
                    })
                    .then((res) => {
                        FileDownload(res.data, "myIfcFile.ifc")
                        this.response = res
                        this.loading = false
                        console.log(this.response)
                    })
                    .catch(function (error) {
                        that.loading = false
                        that.response = error.response
                    });
            }
        },
    },
    components: {
        SnackBar
    }
}
</script>
