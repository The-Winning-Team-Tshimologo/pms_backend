package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.Repository.UserRepository;
import com.thewinningteam.pms.Service.UserService;
import com.thewinningteam.pms.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public void saveProfilePicture(Authentication authentication, MultipartFile imageFile) {
        Authentication auth = getAuthentication();

        // Retrieve the authenticated user's ID
        User userDetails = (User) auth.getPrincipal();
        Long userId = userDetails.getUserId();

        // Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert and compress the MultipartFile to byte array
        byte[] imageData;
        try {
            imageData = compressImage(imageFile.getBytes(), "jpg", 0.7f);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read and compress image file", e);
        }

        // Save the compressed image data in the same table as user information
        user.setProfilePicture(imageData);
        userRepository.save(user);

        System.out.println("Saving compressed image for user with ID: " + userId);
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication;
    }

    private byte[] compressImage(byte[] imageBytes, String format, float quality) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(bais);

        // Create a new BufferedImage with the same width and height
        BufferedImage compressedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }

        writer.write(null, new javax.imageio.IIOImage(compressedImage, null, null), param);
        writer.dispose();
        ios.close();
        baos.close();

        return baos.toByteArray();
    }
}
