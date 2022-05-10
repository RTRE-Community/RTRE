<template>
<v-div>
    <v-file-input truncate-length="15" label="Choose Ifc file" outlined dense v-model="file" @change="fileInformation"></v-file-input>
</v-div>
</template>

<script>
export default {
    name: "FileDrop",
    data() {
        return {
            file: null,
        };
    },
    methods: {
        fileInformation() {
            this.$emit("update:file", this.file.name);
        },
    },
};
</script>

<style></style>
