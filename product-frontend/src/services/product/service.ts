import { axiosInstance } from '@/lib/axios';
import { Product } from '@/Types/product';

interface ApiResponse<T> {
    status: string;
    errorCode: string;
    message: string;
    data: T;
    links: Record<string, string>;
}

interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}

export const productService = {
    getAll: async (page = 0, size = 10) => {
    const response = await axiosInstance.get<ApiResponse<PageResponse<Product>>>('/products', {
        params: { page, size }
    });
    return response.data;
},

    getById: async (id: number) => {
    const response = await axiosInstance.get<ApiResponse<Product>>(`/products/${id}`);
    return response.data;
},

    create: async (product: Omit<Product, 'id'>) => {
    const response = await axiosInstance.post<ApiResponse<Product>>('/products', product);
    return response.data;
},

    update: async (id: number, product: Product) => {
    const response = await axiosInstance.put<ApiResponse<Product>>(`/products/${id}`, product);
    return response.data;
},

    delete: async (id: number) => {
    const response = await axiosInstance.delete<ApiResponse<void>>(`/products/${id}`);
    return response.data;
},

    search: async (searchTerm: string, page = 0, size = 10) => {
    const response = await axiosInstance.get<ApiResponse<PageResponse<Product>>>('/products/search', {
        params: { searchTerm, page, size }
    });
    return response.data;
}
};