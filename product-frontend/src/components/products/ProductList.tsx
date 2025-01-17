import { useState } from "react"
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import { Product } from "@/Types/product"
import { ProductForm } from "./ProductForm"
import { productService } from "@/services/product/service"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import { useToast } from "@/hooks/use-toast"
import { Pencil, Trash2 } from "lucide-react"

export const ProductList = () => {
    const { toast } = useToast()
    const queryClient = useQueryClient()
    const [page, setPage] = useState(0)
    const [searchTerm, setSearchTerm] = useState("")
    const [editingProduct, setEditingProduct] = useState<Product | null>(null)

    const { data: productsData, isLoading } = useQuery({
        queryKey: ["products", page, searchTerm],
        queryFn: async () => {
            if (searchTerm) {
                return await productService.search(searchTerm, page)
            }
            return await productService.getAll(page)
        },
    })

    const deleteMutation = useMutation({
        mutationFn: async (id: number) => {
            return await productService.delete(id)
        },
        onSuccess: (responseData) => {
            queryClient.invalidateQueries({ queryKey: ["products"] })
            toast({
                title: "Produit supprimé",
                description: responseData.message,
            })
        },
        onError: (error: any) => {
            toast({
                variant: "destructive",
                title: "Erreur",
                description: error.response?.data?.message || "Une erreur est survenue",
            })
        },
    })

    if (isLoading) {
        return <div className="text-center">Chargement...</div>
    }

    return (
        <div className="space-y-6 p-3">
            <div className="flex justify-between items-center">
                <Input
                    type="search"
                    placeholder="Rechercher..."
                    className="max-w-sm"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />

                <Dialog>
                    <DialogTrigger asChild>
                        <Button>Nouveau produit</Button>
                    </DialogTrigger>
                    <DialogContent>
                        <DialogHeader>
                            <DialogTitle>Créer un nouveau produit</DialogTitle>
                        </DialogHeader>
                        <ProductForm onSuccess={() => setEditingProduct(null)} />
                    </DialogContent>
                </Dialog>
            </div>

            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead>Nom</TableHead>
                        <TableHead>Description</TableHead>
                        <TableHead>Prix</TableHead>
                        <TableHead className="w-24">Actions</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {productsData?.data.content.map((product: Product) => (
                        <TableRow key={product.id}>
                            <TableCell>{product.name}</TableCell>
                            <TableCell>{product.description}</TableCell>
                            <TableCell>{product.price.toFixed(2)}€</TableCell>
                            <TableCell>
                                <div className="flex space-x-2">
                                    <Dialog>
                                        <DialogTrigger asChild>
                                            <Button
                                                variant="outline"
                                                size="icon"
                                                onClick={() => setEditingProduct(product)}
                                            >
                                                <Pencil className="h-4 w-4" />
                                            </Button>
                                        </DialogTrigger>
                                        <DialogContent>
                                            <DialogHeader>
                                                <DialogTitle>Modifier le produit</DialogTitle>
                                            </DialogHeader>
                                            <ProductForm
                                                initialData={product}
                                                onSuccess={() => setEditingProduct(null)}
                                            />
                                        </DialogContent>
                                    </Dialog>

                                    <AlertDialog>
                                        <AlertDialogTrigger asChild>
                                            <Button variant="destructive" size="icon">
                                                <Trash2 className="h-4 w-4" />
                                            </Button>
                                        </AlertDialogTrigger>
                                        <AlertDialogContent>
                                            <AlertDialogHeader>
                                                <AlertDialogTitle>Supprimer le produit</AlertDialogTitle>
                                                <AlertDialogDescription>
                                                    Êtes-vous sûr de vouloir supprimer ce produit ? 
                                                    Cette action ne peut pas être annulée.
                                                </AlertDialogDescription>
                                            </AlertDialogHeader>
                                            <AlertDialogFooter>
                                                <AlertDialogCancel>Annuler</AlertDialogCancel>
                                                <AlertDialogAction
                                                    onClick={() => product.id && deleteMutation.mutate(product.id)}
                                                >
                                                    Supprimer
                                                </AlertDialogAction>
                                            </AlertDialogFooter>
                                        </AlertDialogContent>
                                    </AlertDialog>
                                </div>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>

            <div className="flex justify-center space-x-2">
                <Button
                    variant="outline"
                    disabled={page === 0}
                    onClick={() => setPage(p => p - 1)}
                >
                    Précédent
                </Button>
                <Button
                    variant="outline"
                    onClick={() => setPage(p => p + 1)}
                >
                    Suivant
                </Button>
            </div>
        </div>
    )
}