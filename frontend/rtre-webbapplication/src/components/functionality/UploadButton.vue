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
            console.log(this.fileupload)
            formData.append("file", this.fileupload)
            let response = await fetch('http://localhost:3030/api/upload', {
                xhr: function(){
                    var xhr = new Window.XMLHttpRequest();

                    xhr.upload.addEventListener('progress', function(e){

                        if (e.lengthComputable){

                            console.log('Bytes Loaded: ' + e.loaded);
                            console.log('Total Size: ' + e.total);
                            console.log('Percentage Uploaded: ' + (e.total/e.loaded))
                        }
                    });

                    
                },
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
