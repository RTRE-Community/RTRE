<template>
<div>
    <v-form ref="valid">
        <v-container>
            <v-row>
                <v-col cols="12" md="6">
                    <v-text-field v-model="name" :rules="ruleInput" :counter="10" label="Project name" required></v-text-field>
                </v-col>
                <v-col cols="12" md="6">
                    <v-file-input v-model="fileupload" :rules="ruleFiles" label="File " required></v-file-input>
                </v-col>
                <v-col cols="12" md="12">
                    <v-textarea v-model="description" :rules="ruleInput" label="" required></v-textarea>
                </v-col>
                <v-col>
                    <div class="d-flex flex-row-reverse">
                        <v-btn class="ml-11" color="blue white--text" :loading="loading[0]" @click="checkIn()">
                            Submit
                        </v-btn>
                    </div>
                </v-col>
            </v-row>
        </v-container>
    </v-form>
    <SnackBar :response="response"></SnackBar>
</div>
</template>

<script>
import SnackBar from '../buttons/SnackBar.vue'
import Vue from "vue";
export default {
    data() {
        return {
            ruleFiles: [(value) => value.size >= 0 || "need a file!"],
            ruleInput: [(value) => !!value || "input an id"],
            loading: [false],
            response: "",
            fileupload: [],
            name: null,
            description: null,
        };
    },
    props: ["parentOid"],
    methods: {
        async checkIn() {
            if (this.$refs.valid.validate()) {
                Vue.set(this.loading, 0, true);
                let formData = new FormData();
                formData.append("file", this.fileupload);
                this.response = await fetch(
                    process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/postIfcAsSubProject?parentPoid=" +
                    this.parentOid + "&name=" + this.name + "&description=" + this.description
                    , {
                        method: "POST",
                        body: formData,
                    }
                );
            }
            Vue.set(this.loading, 0, false);
        },
    },
    components: { SnackBar }
}
</script>
