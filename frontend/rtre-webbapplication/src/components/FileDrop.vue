<template>
<div>
    <v-file-input truncate-length="30" label="Choose Ifc file" outlined dense v-model="file" @change="fileInformation" class="shrink mr-7"></v-file-input>
</div>
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
