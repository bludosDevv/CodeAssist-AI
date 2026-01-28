package com.tyron.code.ai.operations;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileOperationsManager {
    private static final String TAG = "FileOperationsManager";
    private final File projectRoot;
    
    public FileOperationsManager(File projectRoot) {
        this.projectRoot = projectRoot;
    }
    
    /**
     * Read file content
     */
    public String readFile(String relativePath) throws IOException {
        File file = new File(projectRoot, relativePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File not found: " + relativePath);
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    /**
     * Write content to file (creates if doesn't exist)
     */
    public void writeFile(String relativePath, String content) throws IOException {
        File file = new File(projectRoot, relativePath);
        File parentDir = file.getParentFile();
        
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
        Log.d(TAG, "File written: " + relativePath);
    }
    
    /**
     * Create a new file
     */
    public boolean createFile(String relativePath) throws IOException {
        File file = new File(projectRoot, relativePath);
        File parentDir = file.getParentFile();
        
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
        
        boolean created = file.createNewFile();
        Log.d(TAG, "File created: " + relativePath + " - " + created);
        return created;
    }
    
    /**
     * Create a new directory
     */
    public boolean createDirectory(String relativePath) {
        File dir = new File(projectRoot, relativePath);
        boolean created = dir.mkdirs();
        Log.d(TAG, "Directory created: " + relativePath + " - " + created);
        return created;
    }
    
    /**
     * Delete file or directory
     */
    public boolean delete(String relativePath) {
        File file = new File(projectRoot, relativePath);
        boolean deleted = deleteRecursive(file);
        Log.d(TAG, "Deleted: " + relativePath + " - " + deleted);
        return deleted;
    }
    
    private boolean deleteRecursive(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        return file.delete();
    }
    
    /**
     * List files in directory
     */
    public List<String> listFiles(String relativePath) {
        List<String> fileList = new ArrayList<>();
        File dir = new File(projectRoot, relativePath);
        
        if (!dir.exists() || !dir.isDirectory()) {
            return fileList;
        }
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getName());
            }
        }
        
        return fileList;
    }
    
    /**
     * Check if file exists
     */
    public boolean exists(String relativePath) {
        File file = new File(projectRoot, relativePath);
        return file.exists();
    }
    
    /**
     * Rename/Move file
     */
    public boolean rename(String oldPath, String newPath) {
        File oldFile = new File(projectRoot, oldPath);
        File newFile = new File(projectRoot, newPath);
        
        File newParent = newFile.getParentFile();
        if (newParent != null && !newParent.exists()) {
            newParent.mkdirs();
        }
        
        boolean renamed = oldFile.renameTo(newFile);
        Log.d(TAG, "Renamed: " + oldPath + " -> " + newPath + " - " + renamed);
        return renamed;
    }
    
    /**
     * Get project structure as string
     */
    public String getProjectStructure() {
        StringBuilder structure = new StringBuilder();
        buildStructure(projectRoot, "", structure, 0, 3);
        return structure.toString();
    }
    
    private void buildStructure(File dir, String prefix, StringBuilder sb, int depth, int maxDepth) {
        if (depth > maxDepth) return;
        
        File[] files = dir.listFiles();
        if (files == null) return;
        
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            
            // Skip hidden files and build directories
            if (name.startsWith(".") || name.equals("build") || name.equals("bin")) {
                continue;
            }
            
            boolean isLast = (i == files.length - 1);
            sb.append(prefix);
            sb.append(isLast ? "└── " : "├── ");
            sb.append(name);
            
            if (file.isDirectory()) {
                sb.append("/");
            }
            sb.append("\n");
            
            if (file.isDirectory()) {
                String newPrefix = prefix + (isLast ? "    " : "│   ");
                buildStructure(file, newPrefix, sb, depth + 1, maxDepth);
            }
        }
    }
    
    public File getProjectRoot() {
        return projectRoot;
    }
}
