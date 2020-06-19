package com.frameworkium.lite.htmlelements.element;

import com.frameworkium.lite.common.properties.Property;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.frameworkium.lite.htmlelements.utils.HtmlElementUtils.*;

/** Represents web page file upload element. */
public class FileInput extends TypifiedElement {

    /**
     * Specifies wrapped {@link WebElement}.
     *
     * @param wrappedElement {@code WebElement} to wrap.
     */
    public FileInput(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * Sets a file to be uploaded.
     * <p>
     * File is searched in the following way: if a resource with a specified name exists in classpath,
     * then this resource will be used, otherwise file will be searched on file system.
     *
     * @param fileName Name of a file or a resource to be uploaded.
     */
    public void setFileToUpload(final String fileName) {
        // Proxy can't be used to check the element class, so find real WebElement
        WebElement fileInputElement = getNotProxiedInputElement();
        // Set local file detector in case of remote driver usage
        if (Property.GRID_URL.isSpecified()
                || isOnRemoteWebDriver(fileInputElement)) {
            setLocalFileDetector((RemoteWebElement) fileInputElement);
        }

        String filePath = getFilePath(fileName);
        fileInputElement.sendKeys(filePath);
    }

    /**
     * Sets multiple files to be uploaded.
     * <p>
     * Files are searched in the following way:
     * if a resource with a specified name exists in classpath,
     * then this resource will be used, otherwise file will be searched on file system.
     *
     * @param fileNames a list of file Names to be uploaded.
     */
    public void setFilesToUpload(List<String> fileNames) {
        // Proxy can't be used to check the element class, so find real WebElement
        WebElement fileInputElement = getNotProxiedInputElement();
        // Set local file detector in case of remote driver usage
        if (Property.GRID_URL.isSpecified()
                || isOnRemoteWebDriver(fileInputElement)) {
            setLocalFileDetector((RemoteWebElement) fileInputElement);
        }

        String filePaths = fileNames.stream()
                .map(this::getFilePath)
                .collect(Collectors.joining("\n"));
        fileInputElement.sendKeys(filePaths);
    }

    /**
     * Submits selected file by simply submitting the whole form, which contains this file input.
     */
    public void submit() {
        getWrappedElement().submit();
    }

    private WebElement getNotProxiedInputElement() {
        return getWrappedElement().findElement(By.xpath("."));
    }

    private void setLocalFileDetector(RemoteWebElement element) {
        element.setFileDetector(new LocalFileDetector());
    }

    private String getFilePath(final String fileName) {
        if (existsInClasspath(fileName)) {
            return getPathForResource(fileName);
        }
        return getPathForSystemFile(fileName);
    }

    private String getPathForResource(final String fileName) {
        return getResourceFromClasspath(fileName).getPath();
    }

    private String getPathForSystemFile(final String fileName) {
        File file = new File(fileName);
        return file.getPath();
    }
}
