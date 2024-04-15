export interface Task {
    id?: number;
    taskName: string;
    taskDescription: string;
    deadline: string;
    taskDate?: string;
    taskTime?: string;
    categoryName?: string;
    category?: number;
    categoryId?: number;
    status: number;
}