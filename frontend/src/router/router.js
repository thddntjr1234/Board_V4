import {createWebHistory, createRouter} from "vue-router";
import ErrorPage from "@/views/ErrorView.vue";
import FreeBoardView from "@/views/FreeBoardView.vue";
import MainPage from "@/views/MainPage.vue";
import PostWriteFormView from "@/views/PostWriteFormView.vue";
import PostView from "@/views/PostView.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [ // path별 component를 추가한다.
        {
            path: "/",
            name: "mainPage",
            component: MainPage
        },
        {
            path: "/boards/free",
            name: "freeBoardView",
            component: FreeBoardView
        },
        {
            path: "/boards/free/new",
            name: "newForm",
            component: PostWriteFormView
        },
        {
            path: "/:pathMatch(.*)",
            name: "not-found",
            component: ErrorPage
        },
        {
            path: "/boards/free/:postId",
            name: "PostView",
            component: PostView
        }
    ]
});

export default router;