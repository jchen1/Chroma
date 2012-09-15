#include <jni.h>

#define MAX(x, y) 	return (x > y ? x :  y);
#define MIN(x, y) 	return (x < y ? x :  y);
#define ABS(x)		return (x > 0 ? x : -x);

JNIEXPORT void JNICALL Java_chroma_CBFilter_filterRedGreen(
	JNIEnv *env, jobject this, jintArray data, jintArray filtered, jint width, jint height);

void hsl2rgb(int h, int s, int l, int* r, int* g, int*b);
void rgb2hsl(int r, int g, int b, int* h, int* s, int* l);

JNIEXPORT void JNICALL Java_chroma_CBFilter_filterRedGreen(
	JNIEnv *env, jobject this, jintArray data, jintArray filtered, jint width, jint height)
{
	jint *dest_buf = (jint*) ((*env)->GetIntArrayElements(env, NULL));

	jboolean frame_copy;
	jint *src_buf = (*env)->GetIntArrayElements(env, data, NULL);

	for (int i = 0; i < width * height; i++)
	{
		int r = (src_buf[i] >> 16) & 0xFF;
        int g = (src_buf[i] >> 8)  & 0xFF;
        int b = (src_buf[i])       & 0xFF;
        int h = 0, s = 0, l = 0;

        rgb2hsl(r, g, b, &h, &s, &l);

        int h = hsl[0], s = hsl[1], l = hsl[2];

        if (h >= 120 && h < 140 || h <= 215 && h > 190)
        {
            s *= (0.000000410256 * h * h * h * h - 0.000274872 * h * h * h + 0.0685359 * h * h - 7.53578 * h + 308.285);
            if (s < 0) s = 0;    
        }
        else if (h >= 140 && h <= 190) s = 0;

        //Shift most green up to where blue used to be
        if (h > 42 && h < 120) h += 70; 

        hsl2rgb(h, s, l, &r, &g, &b);

        dest_buf[i] = (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);
	}
	ReleaseIntArrayElements(env, filtered, dest_buf, 0);
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
    *h = (h_prime / 6) - (1 << 7);
    *l = (max + min) >> 1;
    *s = 0;
    if (c != 0)
    {
        int divisor = 1 - ABS(2 * *l - 1);
        *s = (c << 8) / (divisor);
    }
}