<template>
<v-card flat>
    <v-card-text>
        <v-form ref="form">
            <v-text-field :rules="ruleInput" label="id" v-model="id" outlined class="shrink mx-11"></v-text-field>
            <v-btn text class="blue white--text mx-0 mt-3" @click="checkOut" :loading="loading[0]">Get Project</v-btn>
        </v-form>
    </v-card-text>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import FileDownload from "js-file-download"
import Axios from "axios"
import SnackBar from "./buttons/SnackBar.vue";
import Vue from 'vue'
export default {
    name: "checkOut",
    data() {
        return {
            ruleInput: [
                value => value >= 3 || 'input an id'
            ],
            id: "",
            response: "",
            loading: [false]
        };
    },
    methods: {
        async checkOut() {
            if (this.$refs.form.validate()) {
                let that = this;
                Vue.set(this.loading, 0, true)
                await Axios({
                        url: process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getIfc?fileName=" + this.id,
                        methods: "GET",
                        responseType: "blob"
                    })
                    .then((res) => {
                        FileDownload(res.data, "myIfcFile.ifc")
                        this.response = res
                        console.log(this.response)
                    })
                    .catch(function (error) {
                        that.response = error.response
                    });
            }
            Vue.set(this.loading, 0, false)

        },
    },
    components: {
        SnackBar
    }
}
</script>
