<template>
<v-app class="ma-10">
    <TopBar></TopBar>
    <Upload></Upload>
    <v-container class="mt-16">
        <v-layout row>
            <v-flex xs12 md6>
                <v-container class="mb-12">
                    <FunctionHub />
                </v-container>
            </v-flex>
            <v-flex xs12 md6>
                <v-container>
                    <SearchBox></SearchBox>
                </v-container>
            </v-flex>
            <v-flex xs12 md 12>
                <v-container class="py-10">
                    <ModelViewer />
                </v-container>
            </v-flex>
        </v-layout>
    </v-container>
</v-app>
</template>

<script>
import FunctionHub from "./components/FunctionHub.vue";
import ModelViewer from "./components/ModelViewer.vue";
import SearchBox from "./components/SearchBox.vue";
import TopBar from "./components/TopBar.vue";
import Upload from "./components/functionality/UploadButton.vue";
export default {
    name: "App",

    components: {
    FunctionHub,
    ModelViewer,
    SearchBox,
    TopBar,
    Upload
},

    data: () => ({
        //
    }),
};
</script>
