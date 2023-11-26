export interface ToPage<T> {
  content: T[];
  pageSize: number;
  currentPage: number;
  totalElements: number;
}
