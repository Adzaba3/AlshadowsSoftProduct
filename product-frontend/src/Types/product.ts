import { z } from "zod";

export const ProductSchema = z.object({
    id: z.number().optional(),
    name: z.string().min(1, "Le nom est requis"),
    description: z.string().min(1, "La description est requise"),
    price: z.number().min(0, "Le prix doit être positif"),
    creationDate: z.string().datetime().optional(),
    updateDate: z.string().datetime().optional()
});

export type Product = z.infer<typeof ProductSchema>;

export type ApiResponse<T> = {
    status: string;
    errorCode: string;
    message: string;
    data: T;
    links: Record<string, string>;
};