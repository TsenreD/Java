package itmo.ja.dyagin.implementor;

import javax.tools.JavaCompiler;
import java.nio.charset.StandardCharsets;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;


/**
 * Class for generating compilable extension files and jar files.
 * <p>
 * Has two modes: {@link #implementJar(Class, Path)} and {@link  #implement(Class, Path)}
 */
public class Implementor implements JarImpler {
    /**
     * Basic constructor for {@code Implementor class}
     */
    public Implementor() {

    }

    /**
     * Command line tool for {@code Implementor}.
     * <br>
     * Correct usage is {@code --jar <className> <pathToJar>}.
     * It is equivalent to calling
     * {@link #implementJar(Class, Path)}
     *
     * @param args list of command line arguments
     * @throws ImplerException if jar couldn't be implemented
     */
    public static void main(String[] args) throws ImplerException {
        if (args.length != 3) {
            printValidUsage();
            return;
        }
        requireNonNull(args[0], args[1], args[2]);
        if (!args[0].equals("-jar") || !args[2].endsWith(".jar")) {
            printValidUsage();
            return;
        }
        try {
            new Implementor().implementJar(Class.forName(args[1]), Paths.get(args[2]));
        } catch (ClassNotFoundException e) {
            throw new ImplerException("Invalid class", e);
        } catch (InvalidPathException e) {
            throw new ImplerException("Invalid path", e);
        }
    }


    /**
     * Method to require all passed strings to be non-null.
     * <br>
     * Calls {@link Objects#requireNonNull(Object)} on every argument.
     *
     * @param args list of arguments to check nullability of
     */
    private static void requireNonNull(String... args) {
        for (Object obj : args) {
            Objects.requireNonNull(obj);
        }
    }

    /**
     * Prints the correct usage of the main class.
     *
     * @see #main(String[])
     */
    private static void printValidUsage() {
        System.out.println("Usage: java Implementor.java -jar <canonical className> <pathToJar>");
    }


    /**
     * File visitor that deletes files and directories.
     *
     * @see SimpleFileVisitor
     */
    private static final SimpleFileVisitor<Path> DELETE_VISITOR = new SimpleFileVisitor<>() {

        /**
         * Deletes given file.
         * @param file file to delete.
         */
        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }


        /**
         * Deletes given directory.
         * @param dir file to delete.
         */
        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    /**
     * Suffix for java files.
     */
    private static final String JAVA_SUFFIX = ".java";

    /**
     * Returns simple name of a class ending with "Impl".
     *
     * @param token class to return the name of
     * @return the modified name
     */
    private String getClassNameImpl(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }

    /**
     * Returns path of the given token resolved according to the given path.
     *
     * @param token class to get the path of.
     * @param root  path that will be used as a parent path.
     * @return the complete path of the class.
     */
    private Path getPath(Class<?> token, Path root) {
        String dir = token.getPackageName().replace(".", File.separator);
        String className = getClassNameImpl(token) + JAVA_SUFFIX;
        return root.resolve(Path.of(dir, className));
    }

    /**
     * Creates the parent directory of a given path, if it is not null.
     *
     * @param path path from which the parent directory will be created.
     * @throws ImplerException if parent of the given directory is not null and creation fails.
     */
    private void createDir(Path path) throws ImplerException {
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new ImplerException("Unable to create a directory", e);
            }
        }
    }

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (!isImplementable(token)) {
            throw new ImplerException("Cannot implement " + token.getCanonicalName());
        }
        Path toImpl = getPath(token, root);
        createDir(toImpl);
        try (BufferedWriter writer = Files.newBufferedWriter(toImpl)) {
            writer.write(classInfo(token));
        } catch (IOException e) {
            throw new ImplerException("Unable to write to Impl class file", e);
        }
    }

    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        createDir(jarFile);
        Path temp = createTempDir(jarFile);
        try {
            implement(token, temp);
            compile(token, temp);
            createJar(token, jarFile, temp);
        } finally {
            clean(temp);
        }

    }

    /**
     * Creates temporary directory at the specified path's parent.
     *
     * @param root path from which the parent directory will be created.
     * @return the created temporary directory.
     * @throws ImplerException if given directory is null or creation fails.
     * @see Files#createTempDirectory(Path, String, FileAttribute[])
     */
    private Path createTempDir(Path root) throws ImplerException {
        if (root == null) {
            throw new ImplerException("Invalid path");
        }
        try {
            return Files.createTempDirectory(root.toAbsolutePath().getParent(), "temp-jar");
        } catch (IOException e) {
            throw new ImplerException("Cannot create temporary directory", e);
        }
    }


    /**
     * Creates temporary directory at the specified path's parent.
     *
     * @param token   class which will be packed in the jar file.
     * @param jarFile path where jar file will be created.
     * @param root    path with all necessary compiled classes for jar.
     * @throws ImplerException if creating jar file fails.
     * @see JarOutputStream
     */
    private void createJar(Class<?> token, Path jarFile, Path root) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            String localName = token.getPackageName().replace(".", "/") + "/"
                    + getClassNameImpl(token) + ".class";
            out.putNextEntry(new ZipEntry(localName));
            Files.copy(root.resolve(localName), out);
        } catch (IOException e) {
            throw new ImplerException("Couldn't create a jar file", e);
        }
    }

    /**
     * Deletes files and directories recursively using {@link #DELETE_VISITOR}.
     *
     * @param root the starting file.
     * @throws ImplerException if deletion failed.
     * @see Files#walkFileTree(Path, FileVisitor)
     */
    private static void clean(final Path root) throws ImplerException {
        try {
            if (Files.exists(root)) {
                Files.walkFileTree(root, DELETE_VISITOR);
            }
        } catch (IOException e) {
            throw new ImplerException("Couldn't clean the directory", e);
        }
    }


    /**
     * Gets path of the class for {@link #compile(Class, Path)}
     *
     * @param token class of which to get the path of.
     * @return string value of the requested path.
     */
    private static String getClassPath(Class<?> token) {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Compiles the class at the specified path.
     *
     * @param token the class which will be compiled
     * @param file  the path at which class will be compiled.
     * @throws ImplerException if compiler was null or compilation failed.
     * @see JavaCompiler
     */
    private void compile(Class<?> token, Path file) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final String classpath = file + File.pathSeparator + getClassPath(token);
        final String[] args = new String[]{"-encoding", StandardCharsets.UTF_8.name(), "-cp", classpath, getPath(token, file).toString()};
        if (compiler == null || compiler.run(null, null, null, args) != 0) {
            throw new ImplerException("Couldn't compile class " + token);
        }
    }

    /**
     * Checks if the given class is possible to implement.
     *
     * @param token the class to check.
     * @return {@code true} if class is implementable, {@code false} otherwise.
     */
    private boolean isImplementable(Class<?> token) {
        int mods = token.getModifiers();
        return !token.isPrimitive() && !token.isArray() && token != Enum.class
                && !Modifier.isFinal(mods) && !Modifier.isPrivate(mods);
    }

    /**
     * System-dependant line separator.
     */
    private static final String LINE_SEP = System.lineSeparator();

    /**
     * Gets compilable package name of the token.
     *
     * @param token the class from which to get the package.
     * @return compile-ready package name of the specified class.
     */
    private String getPackage(Class<?> token) {
        return token.getPackage() + ";";
    }

    /**
     * Joins package of the given class and generated body.
     *
     * @param token the class to create a body for.
     * @return generated compile-ready class.
     * @throws ImplerException
     */
    private String classInfo(Class<?> token) throws ImplerException {
        return join(getPackage(token), LINE_SEP, getClass(token));
    }

    /**
     * Gets compile-ready body of the class.
     *
     * @param token the class to create a body for.
     * @return generated class body for specified class.
     * @throws ImplerException
     */
    private String getClass(Class<?> token) throws ImplerException {
        return String.format("public class %s %s %s { %s %s %s}",
                getClassNameImpl(token),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName(),
                LINE_SEP,
                getClassBody(token),
                LINE_SEP);
    }

    /**
     * Joins generated methods and constructors of the given class.
     *
     * @param token the class to create methods and constructors for.
     * @return line-separated constructors and methods of the given class.
     */
    private String getClassBody(Class<?> token) throws ImplerException {
        return join(getConstructors(token), LINE_SEP, getUniqueMethods(token));
    }


    /**
     * Generates constructors for the given class.
     *
     * @param token the class to create constructors for.
     * @return line-separated constructors.
     */
    private String getConstructors(Class<?> token) throws ImplerException {
        if (token.isInterface()) {
            return "";
        }
        List<Constructor<?>> toImplement = getValidConstructors(token);
        if (toImplement.isEmpty()) {
            throw new ImplerException("Only private constructors in " + token.getCanonicalName());
        }
        return join(toImplement, this::constructorToCompilable);
    }

    /**
     * Filters class constructor list, removing that which are private.
     *
     * @param token the class to filter constructors for.
     * @return list of non-private constructors.
     */
    private List<Constructor<?>> getValidConstructors(Class<?> token) {
        return Arrays.stream(token.getDeclaredConstructors())
                .filter(c -> !Modifier.isPrivate(c.getModifiers()))
                .toList();
    }

    /**
     * Generates compilable constructor body for the given constructor.
     *
     * @param constructor the constructor to generate the body for.
     * @return generated constructor body.
     */
    private String constructorToCompilable(Constructor<?> constructor) {
        return buildMethod(
                getModifiers(constructor), "",
                getConstructorBody(constructor.getParameters()),
                getClassNameImpl(constructor.getDeclaringClass()),
                constructor.getParameters(), constructor.getExceptionTypes());
    }

    /**
     * Generates compilable method body for the given method.
     *
     * @param method the method to generate the body for.
     * @return generated method body.
     */
    private String methodToCompilable(Method method) {
        return buildMethod(getModifiers(method),
                method.getReturnType().getCanonicalName(),
                defaultReturnValue(method.getReturnType()),
                method.getName(), method.getParameters(), method.getExceptionTypes());
    }

    /**
     * Generates compilable method body.
     *
     * @param access         access modified
     * @param returnType     method return type
     * @param body           body of the method
     * @param name           method name
     * @param parameterTypes method parameter types
     * @param exceptionTypes method exception types
     * @return generated method body.
     */
    private String buildMethod(String access, String returnType, String body, String name,
                               Parameter[] parameterTypes, Class<?>[] exceptionTypes) {
        return String.format("%s %s %s (%s) %s {%s %s; %s}",
                access, returnType, name,
                transformParameters(parameterTypes, true), transformExceptions(exceptionTypes),
                LINE_SEP,
                body,
                LINE_SEP);
    }


    /**
     * Generates compilable constructor body with given parameter types.
     *
     * @param parameterTypes the parameters to be included.
     * @return generated constructor body.
     */
    private String getConstructorBody(Parameter[] parameterTypes) {
        return String.format("super(%s)", transformParameters(parameterTypes, false));
    }

    /**
     * Gets decoded java lang accessibility modifiers of the element
     *
     * @param element parameter, from which modifiers will be decoded from
     * @param <T> type of {@link Member} class
     * @return the Java language accessibility modifiers for the underlying member
     * @see #decodeVisibility(int)
     */
    private <T extends Member> String getModifiers(T element) {
        return Modifier.toString(decodeVisibility(element.getModifiers()));
    }


    /**
     * Gets unique implementable method of the given class.
     *
     * @param token class from which methods will be generated from
     * @return unique implementable methods, separated by {@link #LINE_SEP}
     * @see #getImplementableMethods(Class)
     */
    private String getUniqueMethods(Class<?> token) {
        List<Method> implementableMethods = getImplementableMethods(token);
        return join(implementableMethods, this::methodToCompilable);
    }

    /**
     * Gets a list of unique implementable method of the given class.
     *
     * @param token class from which methods will be generated from
     * @return a list of unique implementable methods
     * @see #getAbstractClassMethods(Method[])
     */
    private List<Method> getImplementableMethods(Class<?> token) {
        List<ComparableMethod> abstractMethods = new ArrayList<>(getAbstractClassMethods(token.getMethods()));
        List<ComparableMethod> finalMethods = new ArrayList<>();
        while (token != null) {
            abstractMethods.addAll(getAbstractClassMethods(token.getDeclaredMethods()));
            finalMethods.addAll(getFinalClassMethods(token.getDeclaredMethods()));
            token = token.getSuperclass();
        }
        abstractMethods.removeAll(finalMethods);
        return filterByInheritance(abstractMethods);
    }


    /**
     * Gets class methods which are abstract.
     *
     * @param methods methods to filter
     * @return a list of abstract methods from the ones given
     * @see #getClassMethodsFilteredByModifier(Method[], Predicate)
     */
    private List<ComparableMethod> getAbstractClassMethods(Method[] methods) {
        return getClassMethodsFilteredByModifier(methods, Modifier::isAbstract);
    }

    /**
     * Gets class methods which are final.
     *
     * @param methods methods to filter
     * @return a list of final methods from the ones given
     * @see #getClassMethodsFilteredByModifier(Method[], Predicate)
     */
    private List<ComparableMethod> getFinalClassMethods(Method[] methods) {
        return getClassMethodsFilteredByModifier(methods, Modifier::isFinal);
    }

    /**
     * Gets unique class methods satisfying the predicate.
     *
     * @param methods   methods to filter
     * @param predicate the filtering predicate
     * @return a set of filtered methods
     * @see ComparableMethod
     */
    private List<ComparableMethod> getClassMethodsFilteredByModifier(Method[] methods, Predicate<Integer> predicate) {
        return Arrays.stream(methods)
                .filter(method -> predicate.test(method.getModifiers()))
                .map(ComparableMethod::new)
                .collect(Collectors.toList());
    }


    /**
     * Filters given list, keeping distinct methods and picking
     * the one with least assignable return type from duplicates.
     *
     * @param abstractMethods list to filter
     * @return a list of filtered methods by elements' return type inheritance
     * @see ComparableMethod
     */
    private List<Method> filterByInheritance(List<ComparableMethod> abstractMethods) {
        Map<ComparableMethod, Method> resultMethods = new HashMap<>();
        for (ComparableMethod compMethod : abstractMethods) {
            resultMethods.merge(compMethod, compMethod.value, methodKeepChild);
        }
        return resultMethods.values().stream().toList();
    }


    /**
     * Function that picks the one method with the lowest child in inheritance tree
     * of two methods' return types.
     *
     * @see Class#isAssignableFrom(Class)
     */
    private final BiFunction<? super Method, ? super Method, Method> methodKeepChild =
            (cur, next) -> {
                if (!next.getReturnType().isAssignableFrom(cur.getReturnType())) {
                    return next;
                }
                return cur;
            };


    /**
     * Joins given elements using transform function and a separator.
     *
     * @param elements  the elements to join
     * @param transform function that transforms an element into a string
     * @param delimiter separator for the resulting strings.
     * @return joined elements
     * @see #join(Collection, Function, String)
     */
    private <T> String join(T[] elements, Function<T, String> transform, String delimiter) {
        return join(Arrays.stream(elements).toList(), transform, delimiter);
    }


    /**
     * Joins given elements using transform function and a whitespace.
     *
     * @param elements the elements to join
     * @return joined elements
     * @see #join(Collection, Function, String)
     */
    private String join(String... elements) {
        return join(elements, Function.identity(), " ");
    }


    /**
     * Joins given elements using transform function and a {@link #LINE_SEP}.
     *
     * @param elements  the elements to join
     * @param transform function that transforms an element into a string
     * @return joined elements
     * @see #join(Collection, Function, String)
     */
    private <T> String join(Collection<T> elements, Function<T, String> transform) {
        return join(elements, transform, LINE_SEP);
    }

    /**
     * Joins given elements using transform function and a separator.
     *
     * @param elements  the elements to join
     * @param transform function that transforms an element into a string
     * @param delimiter separator for the resulting strings.
     * @return joined elements
     */
    private <T> String join(Collection<T> elements, Function<T, String> transform, String delimiter) {
        return elements.stream()
                .map(transform)
                .collect(Collectors.joining(delimiter));
    }


    /**
     * Transforms given parameters into compilable method arguments.
     *
     * @param parameters  the parameters to join
     * @param includeType is {@code false} if no parameter types are necessary
     *                    (i.e. super call), {@code true} otherwise.
     * @return transformed joined parameters, separated by a comma.
     * @see #join(Collection, Function, String)
     */
    private String transformParameters(Parameter[] parameters, boolean includeType) {
        return join(parameters,
                p -> (includeType ? p.getType().getCanonicalName() + " " : "") + p.getName(),
                ", "
        );
    }

    /**
     * Transforms given exceptions into compilable throw expression.
     *
     * @param exceptionTypes the exceptions to join
     * @return an empty string if supplied an empty array,
     * {@code throws exception1, exception2, ...} otherwise
     * @see #join(Collection, Function, String)
     */
    private String transformExceptions(Class<?>[] exceptionTypes) {
        if (exceptionTypes.length == 0) {
            return "";
        }
        return "throws" + " " + join(exceptionTypes, Class::getCanonicalName, ", ");
    }


    /**
     * Gets default return value of the given type.
     *
     * @param type the class to get the return value of.
     * @return {@code return <defaultReturnType>}
     */
    private String defaultReturnValue(Class<?> type) {
        StringBuilder res = new StringBuilder("return ");
        if (type == Boolean.TYPE) {
            res.append("false");
        } else if (!type.isPrimitive()) {
            res.append("null");
        } else if (type != Void.TYPE) {
            res.append("0");
        }
        return res.toString();

    }

    /**
     * Decodes access modifiers of give modifier.
     *
     * @param mod the modifiers to decode
     * @return value of the specific modifier, if it matched, 0 otherwise
     * @see Modifier
     */
    private int decodeVisibility(int mod) {
        if (Modifier.isPrivate(mod)) {
            return Modifier.PRIVATE;
        } else if (Modifier.isPublic(mod)) {
            return Modifier.PUBLIC;
        } else if (Modifier.isProtected(mod)) {
            return Modifier.PROTECTED;
        }
        return 0;
    }


    /**
     * Method wrapper class used to separate different class methods.
     */
    private record ComparableMethod(Method value) {

        /**
         * {@inheritDoc}
         * <p>
         * Two {@link ComparableMethod}s are equal iff their names and parameter types are equals.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ComparableMethod that = (ComparableMethod) o;
            return Objects.equals(value.getName(), that.value.getName())
                    && Arrays.equals(value.getParameterTypes(), that.value.getParameterTypes());
        }

        /**
         * {@inheritDoc}
         * <p>
         * Hashes {@link Method#getName()} and {@link Method#getParameterTypes()} of {@link #value}.
         */
        @Override
        public int hashCode() {
            return Objects.hash(value.getName(), Arrays.hashCode(value.getParameterTypes()));
        }
    }
}
