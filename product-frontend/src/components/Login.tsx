import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { authService } from '../services/auth/service';
import { toast } from 'sonner';

const loginSchema = z.object({
    username: z.string().min(1, 'Le nom d\'utilisateur est requis'),
    password: z.string().min(1, 'Le mot de passe est requis')
});

type LoginForm = z.infer<typeof loginSchema>;

export const Login = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [isLoading, setIsLoading] = useState(false);
    const from = (location.state as any)?.from?.pathname || '/';

    const { register, handleSubmit, formState: { errors } } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema)
});

const onSubmit = async (data: LoginForm) => {
    try {
        setIsLoading(true);
        await authService.login(data);
        navigate(from, { replace: true });
        toast.success('Connexion réussie');
    } catch (error: any) {
        toast.error(error.response?.data?.message || 'Erreur de connexion');
    } finally {
        setIsLoading(false);
    }
};

return (
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
        <Card className="w-full max-w-md">
        <CardHeader>
            <CardTitle>Connexion</CardTitle>
        </CardHeader>
        <CardContent>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <Input
                {...register('username')}
                placeholder="Nom d'utilisateur"
                className={errors.username ? 'border-red-500' : ''}
            />
                {errors.username && (
                <p className="text-red-500 text-sm mt-1">{errors.username.message}</p>
            )}
            </div>

            <div>
                <Input
                {...register('password')}
                type="password"
                placeholder="Mot de passe"
                className={errors.password ? 'border-red-500' : ''}
            />
                {errors.password && (
                <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>
            )}
            </div>

            <Button type="submit" className="w-full" disabled={isLoading}>
                {isLoading ? 'Connexion...' : 'Se connecter'}
            </Button>
        </form>
        </CardContent>
    </Card>
    </div>
);
};