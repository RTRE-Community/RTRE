<template>
<v-tooltip bottom>
    <template v-slot:activator="{on , attrs}">
        <v-btn class="mx-2" fab dark small v-bind="attrs" v-on="on" color="red" @click="deleteWithOid()">

            <v-icon dark>
                mdi-delete-circle
            </v-icon>
        </v-btn>
    </template>
    <span>Delete</span>
</v-tooltip>
</template>

<script>
export default {
    name: "DeleteButton",
    props: ['oid'],
    methods: {
        deleteWithOid() {
            fetch(
                process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/deleteProject?oid=" + this.oid
                )
            
        }
    }
}
</script>
