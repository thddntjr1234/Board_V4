import {createWebHistory, createRouter} from "vue-router";
import ErrorPage from "@/views/ErrorView.vue";
import CommunityBoardView from "@/views/community/CommunityBoardView.vue";
import CommunityView from "@/views/community/CommunityView.vue";
import CommunityWriteFormView from "@/views/community/CommunityWriteFormView.vue";
import MainPage from "@/views/MainPage.vue";
import CommunityModifyView from "@/views/community/CommunityModifyView.vue";
import QnABoardView from "@/views/qna/QnABoardView.vue";
import QnAView from "@/views/qna/QnAView.vue";
import QnAModifyView from "@/views/qna/QnAModifyView.vue";
import QnAWriteFormView from "@/views/qna/QnAWriteFormView.vue";
import InquiryBoardView from "@/views/inquiry/InquiryBoardView.vue";
import InquiryView from "@/views/inquiry/InquiryView.vue";
import InquiryModifyView from "@/views/inquiry/InquiryModifyView.vue";
import InquiryWriteFormView from "@/views/inquiry/InquiryWriteFormView.vue";
import NoticeBoardView from "@/views/Notice/NoticeBoardView.vue";
import NoticeView from "@/views/Notice/NoticeView.vue";
import NoticeModifyView from "@/views/Notice/NoticeModifyView.vue";
import NoticeWriteFormView from "@/views/Notice/NoticeWriteFormView.vue";


const router = createRouter({
    history: createWebHistory(),
    routes: [ // path별 component를 추가한다.
        {
            path: "/",
            name: "MainPage",
            component: MainPage
        },
        {
            path: "/:pathMatch(.*)*",
            name: "not-found",
            component: ErrorPage
        },
        {
            path: "/boards/free",
            name: "CommunityBoardView",
            component: CommunityBoardView
        },
        {
            path: "/boards/free/new",
            name: "CommunityWriteFormView",
            component: CommunityWriteFormView
        },
        {
            path: "/boards/free/:postId/edit",
            name: "CommunityModifyView",
            component: CommunityModifyView
        },
        {
            path: "/boards/free/:postId",
            name: "CommunityView",
            component: CommunityView
        },
        {
            path: "/boards/qna",
            name: "QnABoardView",
            component: QnABoardView
        },
        {
            path: "/boards/qna/:postId",
            name: "QnAView",
            component: QnAView
        },
        {
            path: "/boards/qna/:postId/edit",
            name: "QnAModifyView",
            component: QnAModifyView
        },
        {
            path: "/boards/qna/new",
            name: "QnAWriteFormView",
            component: QnAWriteFormView
        },
        {
          path: "/boards/inquiry",
          name: "InquiryBoardView",
          component: InquiryBoardView
        },
        {
            path: "/boards/inquiry/:postId",
            name: "InquiryView",
            component: InquiryView
        },
        {
            path: "/boards/inquiry/:postId/edit",
            name: "InquiryModifyView",
            component: InquiryModifyView
        },
        {
            path: "/boards/inquiry/new",
            name: "InquiryWriteFormView",
            component: InquiryWriteFormView
        },
        {
            path: "/boards/notice",
            name: "NoticeBoardView",
            component: NoticeBoardView,
        },
        {
            path: "/boards/notice/:postId",
            name: "NoticeView",
            component: NoticeView,
        },
        {
            path: "/boards/notice/:postId/edit",
            name: "NoticeModifyView",
            component: NoticeModifyView
        },
        {
            path: "/boards/notice/new",
            name: "NoticeWriteFormView",
            component: NoticeWriteFormView
        }
    ]
});

export default router;