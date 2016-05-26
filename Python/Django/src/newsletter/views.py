from django.shortcuts import render

# Create your views here.
def home(request):

    # MRH - Point to templates
    return render(request, "home.html", {})