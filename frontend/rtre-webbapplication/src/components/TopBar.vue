<template>
<div>
    <v-toolbar flat class="grey lighten-4">
        <v-toolbar-title class="font-weight-light">RTRE</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <NotificationHubVue></NotificationHubVue>
        </v-toolbar-items>
        <div v-for="rout in routes" :key="rout.name">
            <v-btn v-if="rout.meta.showInBar" text color="grey" @click="redirect(rout.path)">{{ rout.name }}</v-btn>
        </div>
    </v-toolbar>
</div>
</template>

<script>
import routesJson from "../router/routes";
import NotificationHubVue from "./functionality/NotificationHub.vue";
export default {
    name: "TopBar",
    data() {
        return {
            routes: routesJson,
        };
    },
    methods: {
        redirect(page) {
            this.$router.push(page);
        },
    },
    components: {
        NotificationHubVue
    },
};
</script>
