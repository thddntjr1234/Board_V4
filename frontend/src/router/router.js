import {createWebHistory, createRouter} from "vue-router";
import ErrorPage from "@/views/ErrorView.vue";
import Board from "@/views/BoardView.vue";
import MainPage from "@/views/MainPage.vue";
import PostWriteFormView from "@/views/PostWriteFormView.vue";

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
            name: "board",
            component: Board
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
        }
    ]
});

export default router;