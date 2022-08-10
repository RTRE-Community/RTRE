<template>
<v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
            <v-btn color="primary" dark v-bind="attrs" v-on="on">
                <v-icon dark>
                    mdi-cloud-download
                </v-icon>
            </v-btn>
        </template>
        <v-card>
            <v-toolbar flat color="blue" dark class="rounded-0">
                <v-toolbar-title>User Profile</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <div class="px-2">
                        <v-btn color="red" @click="dialog = false">
                            Close
                        </v-btn>
                    </div>
                </v-card-actions>
            </v-toolbar>
            <v-tabs vertical>
                <v-tab>
                    <v-icon left> mdi-archive-arrow-up </v-icon>
                    Check in
                </v-tab>
                <v-tab>
                    <v-icon left> mdi-call-merge </v-icon>
                    Merge
                </v-tab>
                <v-tab-item>
                    <CheckInDialog :parentOid="this.oid"/>
                </v-tab-item>
                <v-tab-item>
                </v-tab-item>
            </v-tabs>
        </v-card>
    </v-dialog>
</v-row>
</template>

<script>
import CheckInDialog from '../Fields/CheckInDialog.vue';
export default {
    name: "ModelViewButton",
    props: ["oid"],
    data: () => ({
        dialog: false,
    }),
    components: {
        CheckInDialog
    }
}
</script>
