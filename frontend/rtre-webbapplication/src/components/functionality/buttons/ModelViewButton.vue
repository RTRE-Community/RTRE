<template>
<v-tooltip bottom>
    <template v-slot:activator="{on , attrs}">
        <v-btn class="mx-2" fab dark small v-bind="attrs" v-on="on" color="grey" @click="installWithOid()">

            <v-icon dark>
                mdi-eye-outline
            </v-icon>
        </v-btn>
    </template>
    <span>View Model</span>

</v-tooltip>
</template>

<script>
export default {
    name: "ModelViewButton",
    props: ['oid'],
    methods: {
        installWithOid() {
            this.$router.push({
                name: "Viewer",
                params: {
                    oid: this.oid
                }
            })
        }
    },
}
</script>
