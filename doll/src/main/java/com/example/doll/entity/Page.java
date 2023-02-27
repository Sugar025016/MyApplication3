package com.example.doll.entity;


/**
 * @author: PageResponse
 * @date: 2021/10/1
 * @description:
 */

public class Page<T> {

    PageSet page;

    T content;

    public PageSet getPageSet() {
        return page;
    }

    public void setPageSet(PageSet pageSet) {
        this.page = pageSet;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }



    public class PageSet {

        int totalPages  ;  //    "total_pages": 1,
        int currentPage;//            "current_page": 1,
        boolean hasPre;//            "has_pre": false,
        boolean hasNext;//            "has_next": false,
        //            "category": null


        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public boolean isHasPre() {
            return hasPre;
        }

        public void setHasPre(boolean hasPre) {
            this.hasPre = hasPre;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }
    }
}
