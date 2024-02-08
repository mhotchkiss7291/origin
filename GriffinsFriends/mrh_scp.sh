
# Login

ssh -p 6969 gichiba@bluezoidberg.net

# Pattern from Griffin
scp -v -P 6969 /run/media/mgkabar/bob/DJI_0013.MP4 gichiba@bluezoidberg.net:~/www/Griffin/

# Copy Template

scp -v -P 6969 /Users/mhotchkiss/Desktop/SD1_RAW/SD1_Nikon_Images/DSC_2192.NEF gichiba@bluezoidberg.net:~/www/mhotchkiss/

# By SD Card
scp -r -v -P 6969 /Users/mhotchkiss/Desktop/SD1_RAW/SD1_Nikon_Images/ gichiba@bluezoidberg.net:~/www/mhotchkiss/RAW_Images

scp -r -v -P 6969 /Users/mhotchkiss/Desktop/Stuff/* gichiba@bluezoidberg.net:~/www/mhotchkiss/RAW_Images/SD1_Hikon_images

