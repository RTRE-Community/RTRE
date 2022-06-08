<template>
<v-card flat>
    <v-card-text>
        <v-form ref="form">
            <h1 class="my-6">First merge file:</h1>
            <v-file-input :rules="ruleFiles" label="upload local file: " truncate-length="15" v-model="fileupload"></v-file-input>

            <h1 class="my-6">Second merge file:</h1>
            <v-text-field :rules="ruleInput" label="Id" solo dense v-model="file2"></v-text-field>
            <v-btn text class="blue white--text mx-0 mt-3" @click="uploadFile" :loading="loading">Merge Projects</v-btn>
        </v-form>
    </v-card-text>
    <SnackBar :response="response"></SnackBar>
</v-card>
</template>

<script>
import SnackBar from './buttons/SnackBar.vue';
export default {
    components: {
        SnackBar
    },
    name: "FunctionHub",
    data() {
        return {
            ruleFiles: [
                value => value.size >= 0 || 'need a file!'
            ],
            ruleInput: [
                value => value >= 3 || 'input an id'
            ],
            fileupload: [],
            response: "",
            file2: null,
            loading: false,
        }
    },
    methods: {
        async uploadFile() {
            if (this.$refs.form.validate()) {
                this.loading = true
                let formData = new FormData();
                formData.append("file", this.fileupload)
                this.response = await fetch("http://localhost:3030/api/merge?mergeFile2=" + this.file2, {
                    method: "POST",
                    body: formData
                }).then(this.loading = false)
            }
        }

    }
}
</script>
