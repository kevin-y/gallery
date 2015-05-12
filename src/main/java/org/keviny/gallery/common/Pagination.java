package org.keviny.gallery.common;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */
public final class Pagination {

		private Long currentPage = 1L;
		private Long totalPages;
		private Long totalRecords;
		private Long recordsPerPage = 20L;
		
		public Pagination() {

		}
		public Long getTotalPages() {
			if(totalRecords % recordsPerPage == 0) {
				return totalRecords / recordsPerPage;
			}
			return totalRecords / recordsPerPage + 1;
		}

		public Long getTotalRecords() {
			return totalRecords;
		}

		public void setTotalRecords(Long totalRecords) {
			this.totalRecords = totalRecords;
		}

		public Long getRecordsPerPage() {
			return recordsPerPage;
		}

		public void setRecordsPerPage(Long recordsPerPage) {
			this.recordsPerPage = recordsPerPage;
		}

		public Long getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(Long currentPage) {
			this.currentPage = currentPage;
		}
		
		public Long getRecordBeginning() {
			return recordsPerPage * (currentPage - 1);
		}

		public Long getRecordEndding() {
			return currentPage * recordsPerPage - 1;
		}

		public boolean hasNextPage() {
			return currentPage < totalPages;
		}

		public boolean hasPreviousPage() {
			return currentPage > 1;
		}

		public Long getNextPage() {
			if(hasNextPage())
				return currentPage + 1;
			return currentPage;
		}
		
		public Long getPreviousPage() {
			if(hasPreviousPage()) 
				return currentPage - 1;
			return currentPage;
		}

}
