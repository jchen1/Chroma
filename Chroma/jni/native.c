#include <jni.h>

#define MAX(x, y) 	(x > y ? x :  y)
#define MIN(x, y) 	(x < y ? x :  y)
#define ABS(x)		(x > 0 ? x : -x)
#define NULL 0

JNIEXPORT void JNICALL Java_com_hivemind_chroma_CBFilter_filterRedGreen(
	JNIEnv *env, jobject this, jintArray data, jintArray filtered, jint width, jint height);

void hsl2rgb(int h, int s, int l, int* r, int* g, int* b);
void rgb2hsl(int r, int g, int b, int* h, int* s, int* l);
void clamp(int* x, int min, int max);

JNIEXPORT void JNICALL Java_com_hivemind_chroma_Vision_yuv2rgb(
	JNIEnv *env, jobject this, jintArray rgb, jbyteArray yuv, jint width, jint height)
{
	jint *dest_buf = (*env)->GetIntArrayElements(env, rgb, NULL);
	jbyte *src_buf = (*env)->GetByteArrayElements(env, yuv, NULL);

	int j, yp;
	for (j = 0, yp = 0; j < height; j++)
	{
		int uvp = width * height + (j >> 1) * width, u = 0, v = 0;
		int i;
		for (i = 0; i < width; i++, yp++)
		{
			int y = (0xFF & ((int) src_buf[yp])) - 16;
			if (y < 0) y = 0;
			if ((i & 1) == 0)
			{
				v = (0xFF & src_buf[uvp++]) - 128;
				u = (0xFF & src_buf[uvp++]) - 128;
			}
			y *= 75;

			int rtmp = y + 102 * v;
			int gtmp = y - 25 * u - 52 * v;
			int btmp = y + 129 * u;

			int r = (rtmp >> 6);
			int g = (gtmp >> 6);
			int b = (btmp >> 6);

			if (r < 0) r = 0;
			if (g < 0) g = 0;
			if (b < 0) b = 0;
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;

			dest_buf[yp] = (0xFF000000) | (r << 16) | (g << 8) | b;
		}
	}
	(*env)->ReleaseIntArrayElements(env, rgb, dest_buf, 0);
	(*env)->ReleaseByteArrayElements(env, yuv, src_buf, JNI_ABORT); //Not using extreme care
}

JNIEXPORT void JNICALL Java_com_hivemind_chroma_CBFilter_filterRedGreen(
	JNIEnv *env, jobject this, jintArray data, jintArray filtered, jint width, jint height)
{
	jint *dest_buf = (*env)->GetIntArrayElements(env, filtered, NULL);
	jint *src_buf = (*env)->GetIntArrayElements(env, data, NULL);

	int i;
	for (i = 0; i < width * height; i++)
	{
		int r = (src_buf[i] >> 16) & 0xFF;
		int g = (src_buf[i] >> 8) & 0xFF;
		int b = (src_buf[i]) & 0xFF;

        int h = 0, s = 0, l = 0;

        rgb2hsl(r, g, b, &h, &s, &l);

        //dest_buf[i] = 0xFFFFFF;

        if (h >= 120 && h < 140 || h <= 215 && h > 190)
        {
            s *= (0.000000410256 * h * h * h * h - 0.000274872 * h * h * h + 0.0685359 * h * h - 7.53578 * h + 308.285);
            if (s < 0) s = 0;    
        }
        else if (h >= 140 && h <= 190) s = 0;

        //Shift most green up to where blue used to be
        if (h > 42 && h < 120) h += 70; 

        hsl2rgb(h, s, l, &r, &g, &b);

        dest_buf[i] = 0xFF000000 | (r << 16) | (g << 8) | (b);
        //dest_buf[i] = 0xFFFFFFFF;
	}
	(*env)->ReleaseIntArrayElements(env, filtered, dest_buf, 0);
	(*env)->ReleaseIntArrayElements(env, data, src_buf, JNI_ABORT);
}

void hsl2rgb(int h, int s, int l, int* r, int* g, int*b)
{
    int v = (l < 128) ? (l * (256 + s)) >> 8 : (((l + s) << 8) - l * s) >> 8;
    if (v <= 0)
    {
        *r = *g = *b = 0;
    }
    else
    {
        int m, sextant, fract, vsf, mid1, mid2;
        
        m = l + l - v;
        h *= 6;
        sextant = h >> 8;
        fract = h - (sextant << 8);
        vsf = v * fract * (v - m) / v >> 8;
        mid1 = m + vsf;
        mid2 = v - vsf;
        switch (sextant)
        {
            case 0: *r = v; *g = mid1; *b = m; break;
            case 1: *r = mid2; *g = v; *b = m; break;
            case 2: *r = m; *g = v; *b = mid1; break;
            case 3: *r = m; *g = mid2; *b = v; break;
            case 4: *r = mid1; *g = m; *b = v; break;
            case 5: *r = v; *g = m; *b = mid2; break;
        }

        //clamp(r, 0, 255);
        //clamp(g, 0, 255);
        //clamp(b, 0, 255);
    }
}

void rgb2hsl(int r, int g, int b, int* h, int* s, int* l)
{
    int max = MAX(r, MAX(g, b));
    int min = MIN(r, MIN(g, b));
    
    int c = max - min;
    
    int h_prime = 0;
    if (c == 0);
    else if (max == r)
    {
        h_prime = ((g - b) << 8) / (c);
        if (h_prime < 0) h_prime += (6 << 8);
    }
    else if (max == g)
    {
        h_prime = ((b - r) << 8) / (c);
        h_prime += (2 << 8);
    }
    else if (max == b)
    {
        h_prime = ((r - g) << 8) / (c);
        h_prime += (4 << 8);
    }
    *h = (h_prime / 6);
    *l = (max + min) >> 1;
    *s = 0;
    if (c != 0)
    {
        int divisor = 255 - ABS(2 * *l - 255);
        *s = (divisor == 0 ? 255 : (c << 8) / (divisor));
    }

    clamp(h, 0, 255);
    clamp(s, 0, 255);
    clamp(l, 0, 255);
}

void clamp(int* x, int min, int max)
{
	*x = MAX(min, MIN(*x, max));
}
