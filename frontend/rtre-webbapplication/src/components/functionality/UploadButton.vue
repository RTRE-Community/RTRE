<template>
<div>
    <v-file-input truncate-length="15" v-model="fileupload"></v-file-input>
    <button id="upload-button" @click="uploadFile">upload</button>
</div>
</template>

<script>
export default {
    name: "UploadButton",
    data() {
        return {
            fileupload: ""
        }
    },
    methods: {
        async uploadFile() {
            let formData = new FormData();
            formData.append("file", this.fileupload)
            let response = await fetch('http://host.docker.internal:3030/api/upload', {
                method: "POST",
                body: formData
            })
            if (response.status == 200) {
                alert("File Successfully uploaded.")
            }
        }

    }
}
</script>
