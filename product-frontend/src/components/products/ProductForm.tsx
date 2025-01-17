import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { Product, ProductSchema } from "@/Types/product"
import { productService } from "@/services/product/service"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { useToast } from "@/hooks/use-toast"

interface ProductFormProps {
    initialData?: Product
    onSuccess?: () => void
}

export const ProductForm = ({ initialData, onSuccess }: ProductFormProps) => {
    const { toast } = useToast()
    const queryClient = useQueryClient()

    const form = useForm<Product>({
        resolver: zodResolver(ProductSchema),
        defaultValues: initialData || {
            name: "",
            description: "",
            price: 0,
        },
    })

    const mutation = useMutation({
        mutationFn: async (data: Product) => {
            if (initialData?.id) {
                return await productService.update(initialData.id, data)
            }
            return await productService.create(data)
        },
        onSuccess: (responseData) => {
            queryClient.invalidateQueries({ queryKey: ["products"] })
            toast({
                title: initialData ? "Produit modifié" : "Produit créé",
                description: responseData.message,
            })
            form.reset()
            onSuccess?.()
        },
        onError: (error: any) => {
            toast({
                variant: "destructive",
                title: "Erreur",
                description: error.response?.data?.message || "Une erreur est survenue",
            })
        },
    })

    const onSubmit = (data: Product) => {
        mutation.mutate(data)
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Nom</FormLabel>
                            <FormControl>
                                <Input {...field} placeholder="Nom du produit" />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Description</FormLabel>
                            <FormControl>
                                <Textarea {...field} placeholder="Description du produit" />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={form.control}
                    name="price"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Prix</FormLabel>
                            <FormControl>
                                <Input 
                                    {...field} 
                                    type="number" 
                                    step="0.01"
                                    onChange={e => field.onChange(parseFloat(e.target.value))}
                                />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <Button 
                    type="submit" 
                    disabled={mutation.isPending}
                    className="w-full"
                >
                    {mutation.isPending ? (
                        "En cours..."
                    ) : initialData ? (
                        "Modifier le produit"
                    ) : (
                        "Créer le produit"
                    )}
                </Button>
            </form>
        </Form>
    )
}