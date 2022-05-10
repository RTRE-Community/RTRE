<template>
<v-card>
    <v-toolbar flat color="blue" dark>
        <v-toolbar-title>User Profile</v-toolbar-title>
    </v-toolbar>
    <v-tabs vertical>
        <v-tab>
            <v-icon left> mdi-archive-arrow-up </v-icon>
            Check in
        </v-tab>
        <v-tab>
            <v-icon left> mdi-cloud-download </v-icon>
            Check Out
        </v-tab>
        <v-tab>
            <v-icon left> mdi-call-merge </v-icon>
            Merge
        </v-tab>
        <v-tab-item>
            <CheckIn />
        </v-tab-item>
        <v-tab-item>
            <CheckOut />
        </v-tab-item>
        <v-tab-item>
            <Merge />
        </v-tab-item>
    </v-tabs>
</v-card>
</template>

<script>
import Merge from "./functionality/Merge.vue"
import CheckOut from "./functionality/CheckOut.vue"
import CheckIn from './functionality/CheckIn.vue'
export default {
    components: {
        Merge,
        CheckOut,
        CheckIn
    },
    name: "FunctionHub",
};
</script>

<style></style>
